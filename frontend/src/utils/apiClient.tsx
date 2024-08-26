import axios from "axios";
import { ReactElement, useContext, useEffect } from "react";
import { useTranslation } from "react-i18next";
import { ErrorType, Language } from "../constants/Const";
import { SnackbarContext } from "../providers/SnackbarProvider";
import { APP_TOKEN } from "../storage/localStorage";

const BACKEND_URL =
  process.env.REACT_APP_BASE_URL || "http://localhost:8080/api";

export const apiClient = axios.create({
  baseURL: `${BACKEND_URL}`,
  timeout: !process.env.REACT_APP_REQUEST_TIMEOUT
    ? 15000
    : parseInt(process.env.REACT_APP_REQUEST_TIMEOUT),
});

interface AxiosInterceptorProps {
  children: ReactElement | null;
}

export const AxiosInterceptor = ({
  children,
}: AxiosInterceptorProps): ReactElement | null => {
  const { showSnackbar } = useContext(SnackbarContext);
  const { t, i18n } = useTranslation();

  const requestInterceptor = apiClient.interceptors.request.use((config) => {
    const token = localStorage.getItem(APP_TOKEN);

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    config.headers["Accept-Language"] =
      i18n.language === Language.PL ? "pl" : "en-US";
    return config;
  });

  const responseInterceptor = apiClient.interceptors.response.use(
    (response) => {
      return response;
    },
    (error) => {
      if (axios.isAxiosError(error)) {
        switch (error.response?.data?.errorType) {
          case ErrorType.USER_ACCOUNT_ALREADY_EXISTS:
            showSnackbar(t("signIn.accountAlreadyExists.label"));
            break;
          case ErrorType.USER_NOT_FOUND:
            showSnackbar(t("signIn.userNotFound.label"));
            break;
          case ErrorType.BAD_CREDENTIALS_EXCEPTIION:
            showSnackbar(t("signIn.wrongPassword.label"));
            break;
          case ErrorType.JWT_INVALID:
            showSnackbar(t("signIn.invalidToken.label"));
            break;
          case ErrorType.RE_CAPTCHA_TOKEN_INVALID:
            showSnackbar(t("signIn.invalidReCaptcha.label"));
            break;
          case ErrorType.JWT_EXPIRED:
            showSnackbar(t("errors.sessionExpired.label"));
            break;
          case ErrorType.TAG_CANNOT_BE_DELETED:
            showSnackbar(t("errors.tagCannotBeRemoved.label"));
            break;
          default:
            showSnackbar(t("errors.default.label"));
        }

        localStorage.removeItem(APP_TOKEN);
      }
    },
  );

  useEffect(() => {
    return () => {
      axios.interceptors.request.eject(requestInterceptor);
      axios.interceptors.response.eject(responseInterceptor);
    };
  });

  return children;
};
