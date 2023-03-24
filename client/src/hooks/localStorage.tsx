import React, {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from "react";

export function useLocalStorage<T>(
  key: string,
  initialValue: T
): [T, (state: T | ((prevState: T) => T)) => void] {
  // State to store our value
  // Pass initial state function to useState so logic is only executed once
  const [storedValue, setStoredValue] = useState(initialValue);

  const getValue = useCallback(() => {
    if (typeof window === "undefined") {
      return initialValue;
    }

    try {
      // Get from local storage by key
      const item = window.localStorage.getItem(key);
      // Parse stored json or if none return initialValue
      return item ? JSON.parse(item) : initialValue;
    } catch (error) {
      // If error also return initialValue
      console.error(error);
      return initialValue;
    }
  }, [initialValue, key]);

  useEffect(() => {
    setStoredValue(getValue());
  }, [getValue]);

  // Return a wrapped version of useState's setter function that ...
  // ... persists the new value to localStorage.
  const setValue = useCallback(
    (value: T | ((prevState: T) => T)) => {
      try {
        // Allow value to be a function so we have same API as useState
        const valueToStore =
          value instanceof Function ? value(storedValue) : value;
        // Save state
        setStoredValue(valueToStore);
        // Save to local storage
        if (typeof window !== "undefined") {
          window.localStorage.setItem(key, JSON.stringify(valueToStore));
        }
      } catch (error) {
        // A more advanced implementation would handle the error case
        console.error(error);
      }
    },
    [key, storedValue]
  );

  return [storedValue, setValue];
}

export function createLocalStorageContext<T>(name: string, initialValue: T) {
  const InternalContext = createContext<
    [T, (state: T | ((prevState: T) => T)) => void]
  >([initialValue, () => {}]);

  return {
    Provider({ children }: { children: React.ReactNode }) {
      const [storedValue, setStoredValue] = useLocalStorage<T>(
        name,
        initialValue
      );

      return (
        <InternalContext.Provider
          value={useMemo(
            () => [storedValue, setStoredValue],
            [storedValue, setStoredValue]
          )}
        >
          {children}
        </InternalContext.Provider>
      );
    },
    useContext: () => useContext(InternalContext),
  };
}
