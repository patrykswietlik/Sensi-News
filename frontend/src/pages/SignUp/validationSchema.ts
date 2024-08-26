import * as Yup from "yup";
import { useTranslation } from "react-i18next";

export interface SignUpFormValues {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
}

export const initialValues: SignUpFormValues = {
  username: "",
  email: "",
  password: "",
  confirmPassword: "",
};

export const getValidationSchema = () => {
  const { t } = useTranslation();

  return Yup.object({
    email: Yup.string()
      .email(t("signIn.emailError.label"))
      .required(t("signIn.emailRequired.label"))
      .min(5, t("signIn.emailTooShort.label"))
      .max(255, t("signIn.emailTooLong.label")),
    password: Yup.string()
      .required(t("signIn.passwordRequired.label"))
      .min(8, t("signIn.passwordTooShort.label"))
      .max(255, t("signIn.passwordTooLong.label")),
    confirmPassword: Yup.string()
      .required(t("signIn.passwordRequired.label"))
      .oneOf([Yup.ref("password")], t("signIn.passwordsMatchError.label"))
      .min(8, t("signIn.passwordTooShort.label"))
      .max(255, t("signIn.passwordTooLong.label")),
    username: Yup.string()
      .required(t("signIn.usernameRequired.label"))
      .min(5, t("signIn.usernameTooShort.label"))
      .max(255, t("signIn.usernameTooLong.label")),
  });
};
