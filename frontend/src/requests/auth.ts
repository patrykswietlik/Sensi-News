import { EndpointConst } from "../constants/EndpointConst";
import { apiClient } from "../utils/apiClient";
import { AuthTokenStorage } from "../storage/localStorage";
import { RE_CAPTCHA_HEADER } from "../constants/Const";

interface SignUpFormValues {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
}

interface SignInFormValues {
  email: string;
  password: string;
}

export interface User {
  id: string;
  email: string;
  username: string;
  role: string;
}

export const signUp = async (
  { email, username, password }: SignUpFormValues,
  token: string,
) => {
  const authTokenStorage = new AuthTokenStorage();

  const captchaHeader = {
    [RE_CAPTCHA_HEADER]: token,
  };

  const response = await apiClient.post(
    EndpointConst.REGISTER,
    {
      email,
      username,
      password,
    },
    { headers: captchaHeader },
  );

  authTokenStorage.saveToken(response.data.data);
};

export const signIn = async (
  { email, password }: SignInFormValues,
  token: string,
) => {
  const authTokenStorage = new AuthTokenStorage();

  const captchaHeader = {
    [RE_CAPTCHA_HEADER]: token,
  };

  const response = await apiClient.post(
    EndpointConst.LOGIN,
    {
      email,
      password,
      token,
    },
    { headers: captchaHeader },
  );
  authTokenStorage.saveToken(response.data.data);
  return response.data.data;
};

export const getUser = async (): Promise<User> => {
  const response = await apiClient.get(EndpointConst.CURRENT_USER);
  return response.data;
};
