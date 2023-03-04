import { createLocalStorageContext } from "./localStorage";

export const { Provider: UserProvider, useContext: useUser } =
  createLocalStorageContext<
    | { id: null }
    | {
        id: number;
        email: string;
        name: string;
        lastname: string;
        roleId: number;
        role: string;
      }
  >("session", {
    id: null,
  });
