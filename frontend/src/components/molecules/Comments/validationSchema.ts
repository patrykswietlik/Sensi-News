import * as Yup from "yup";
import { useTranslation } from "react-i18next";

export const initialCommentValues = {
  addComment: "",
};

export const getValidationSchema = () => {
  const { t } = useTranslation();

  return Yup.object({
    addComment: Yup.string()
      .required(t("articleContent.commentRequired.label"))
      .max(1500, t("articleContent.commentTooLong.label")),
  });
};
