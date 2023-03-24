import axios from "axios";
import { createContext, useContext, useEffect, useMemo } from "react";
import { useLocalStorage } from "./localStorage";

type TextMap = Record<string, Record<string, string>>;
type StateType = TextMap | null;

const InternalContext = createContext<
  [
    StateType,
    (state: StateType | ((prevState: StateType) => StateType)) => void
  ]
>([{}, () => {}]);

export function TextProvider({ children }: { children: React.ReactNode }) {
  const [storedValue, setStoredValue] = useLocalStorage<StateType>(
    "translationtexts",
    null
  );

  useEffect(() => {
    axios.get("/api/text").then((res) => {
      setStoredValue(res.data);
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

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
}

export function useTexts(): Record<string, Record<string, string>> {
  const [texts] = useContext(InternalContext);
  if (texts === null) {
    return new Proxy(
      {},
      {
        has(target, component) {
          if (typeof component === "symbol")
            return Reflect.has(target, component);
          return true;
        },
        get(target, component, receiver) {
          if (typeof component === "symbol")
            return Reflect.get(target, component, receiver);
          return new Proxy(
            {},
            {
              get(target, key, receiver) {
                if (typeof key === "symbol")
                  return Reflect.get(target, key, receiver);
                return "";
              },
            }
          );
        },
      }
    );
  } else {
    return new Proxy(texts, {
      get(target, component, receiver) {
        if (typeof component === "symbol")
          return Reflect.get(target, component, receiver);
        const complw = component.toLowerCase();
        return new Proxy(target[complw] ?? {}, {
          get(target, key, receiver) {
            if (typeof key === "symbol")
              return Reflect.get(target, key, receiver);
            const keylw = key.toLowerCase();
            if (keylw in target) return Reflect.get(target, keylw, receiver);
            return `{${complw}.${keylw}}`;
          },
        });
      },
    });
  }
}
