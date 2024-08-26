import * as Yup from "yup";
import { useTranslation } from "react-i18next";

export const initialTagValues = {
  id: "",
  engName: "",
  plName: "",
  deletedOn: "",
  totalArticles: 0,
  removable: true,
};

export const getValidationSchema = () => {
  const { t } = useTranslation();

  return Yup.object({
    engName: Yup.string().required(t("editTags.engNameRequired.label")),
    plName: Yup.string().required(t("editTags.plNameRequired.label")),
  });
};
