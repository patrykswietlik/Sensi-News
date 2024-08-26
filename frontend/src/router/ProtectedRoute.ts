import React, { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { RoutePath } from "./RoutePath ";
import { AuthTokenStorage } from "../storage/localStorage";
import { AuthContext } from "../providers/AuthProvider";
import { UserRoles } from "../constants/Const";

type RouteType = React.ReactElement | null;

type ProtectedRouteProps = {
  children: RouteType;
  isAdminAllowed?: boolean;
};

export const ProtectedRoute = ({
  children,
  isAdminAllowed = false,
}: ProtectedRouteProps): RouteType => {
  const navigate = useNavigate();
  const authTokenStorage = new AuthTokenStorage();
  const { loggedUser } = useContext(AuthContext);
  const [isLoading, setIsLoading] = React.useState<boolean>(true);

  useEffect(() => {
    const token = authTokenStorage.getToken();
    if (!token) {
      navigate(RoutePath.SIGN_IN);
    }
    if (loggedUser) {
      setIsLoading(true);
      if (isAdminAllowed && loggedUser.role !== UserRoles.ADMIN) {
        navigate(RoutePath.SIGN_IN);
      }
      setIsLoading(false);
    } else {
      setIsLoading(false);
    }
  }, [loggedUser]);

  return !isLoading ? children : null;
};
