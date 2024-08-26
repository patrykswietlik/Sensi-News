import * as Yup from "yup";
import { useTranslation } from "react-i18next";

export interface SignInFormValues {
  email: string;
  password: string;
}

export const initialValues: SignInFormValues = {
  email: "",
  password: "",
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
  });
};
