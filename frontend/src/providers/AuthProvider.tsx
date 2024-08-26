import { createContext, ReactNode, useEffect, useState } from "react";
import { getUser, User } from "../requests/auth";
import { AuthTokenStorage } from "../storage/localStorage";
import { LoadingSpinner } from "../components/atoms/LoadingSpinner";

export interface AuthContextType {
  loggedUser?: User;
  setLoggedUser: (user?: User) => void;
  isLoading: boolean;
}

export const AuthContext = createContext<AuthContextType>({
  loggedUser: undefined,
  setLoggedUser: () => {},
  isLoading: true,
});

type AuthProviderProps = {
  children: ReactNode;
};

export const AuthProvider = ({ children }: AuthProviderProps) => {
  const [loggedUser, setLoggedUser] = useState<User | undefined>(undefined);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const authTokenStorage = new AuthTokenStorage();

  useEffect(() => {
    if (authTokenStorage.isTokenPresent()) {
      setIsLoading(true);
      getUser()
        .then((user) => {
          setLoggedUser(user);
        })
        .finally(() => setIsLoading(false));
    }
  }, []);

  return (
    <AuthContext.Provider value={{ loggedUser, setLoggedUser, isLoading }}>
      {isLoading ? <LoadingSpinner /> : children}
    </AuthContext.Provider>
  );
};
