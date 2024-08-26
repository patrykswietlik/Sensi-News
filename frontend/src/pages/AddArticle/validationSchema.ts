import * as Yup from "yup";
import { useTranslation } from "react-i18next";
import { ArticleCreateFormValues } from "../../requests/article";

export const defaultValues: ArticleCreateFormValues = {
  title: "",
  content: "",
  image: "",
  tags: [],
  articleId: "",
};

export const getValidationSchema = () => {
  const { t } = useTranslation();

  return Yup.object({
    title: Yup.string()
      .required(t("newArticle.titleRequired.label"))
      .min(30, t("newArticle.titleTooShort.label"))
      .max(255, t("newArticle.titleTooLong.label")),
    content: Yup.string()
      .required(t("newArticle.contentRequired.label"))
      .min(30, t("newArticle.contentTooShort.label"))
      .max(5000, t("newArticle.contentTooLong.label")),
    image: Yup.mixed().required(t("newArticle.imageRequired.label")).nullable(),
    tags: Yup.array().min(1, t("newArticle.tagsRequired.label")),
  });
};
