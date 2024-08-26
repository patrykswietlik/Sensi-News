import { useEffect, useState } from "react";
import PublishIcon from "@mui/icons-material/Publish";
import UploadFileIcon from "@mui/icons-material/UploadFile";
import {
  Autocomplete,
  Box,
  Button,
  Checkbox,
  TextField,
  Typography,
} from "@mui/material";
import { useTheme } from "@mui/material/styles";
import useMediaQuery from "@mui/material/useMediaQuery";
import { Field, Form, Formik, FormikHelpers } from "formik";
import { useTranslation } from "react-i18next";
import { getValidationSchema, defaultValues } from "./validationSchema";
import { Heading } from "../../components/atoms/Heading";
import { Language, TextVariant } from "../../constants/Const";
import { color } from "../../styles/colors";
import {
  ArticleCreateFormValues,
  ContentGenerationFormValues,
  createArticle,
  generateContent,
  updateArticle,
} from "../../requests/article";
import { getAllTags } from "../../requests/tags";
import { Tag } from "../../models/tag";
import { RoutePath } from "../../router/RoutePath ";
import { useNavigate } from "react-router-dom";

export interface NewArticleFormProps {
  heading?: string;
  initialValues?: ArticleCreateFormValues;
  fullWidth?: boolean;
  isCreation?: boolean;
  onArticelUpdate?: () => void;
}

export const ArticleForm = ({
  heading,
  initialValues,
  fullWidth,
  isCreation,
  onArticelUpdate,
}: NewArticleFormProps) => {
  const theme = useTheme();
  const sm = useMediaQuery(theme.breakpoints.down("sm"));
  const { t, i18n } = useTranslation();
  const [tags, setTags] = useState<Tag[]>([]);
  const navigate = useNavigate();
  const [imageAdded, setImageAdded] = useState(false);
  const [imagePreviewUrl, setImagePreviewUrl] = useState<string | null>(null);
  const [showImage, setShowImage] = useState(false);

  const fetchTags = async () => {
    const tags = await getAllTags();
    setTags(tags);
  };

  useEffect(() => {
    fetchTags();
  }, []);

  const handleCreate = async (
    values: ArticleCreateFormValues,
    { setSubmitting }: FormikHelpers<ArticleCreateFormValues>,
  ) => {
    const response = await createArticle(values);
    setSubmitting(false);
    navigate(RoutePath.ARTICLE.replace(":id", response.data.id));
  };

  const handleUpdate = async (
    values: ArticleCreateFormValues,
    { setSubmitting }: FormikHelpers<ArticleCreateFormValues>,
  ) => {
    await updateArticle(values);
    setSubmitting(false);
    if (onArticelUpdate) {
      onArticelUpdate();
    }
  };

  // @ts-ignore
  return (
    <Box
      sx={{
        width: fullWidth ? "100%" : "auto",
        backgroundColor: color.white,
        marginTop: "30px",
        padding: sm ? "25px" : "100px",
      }}
    >
      <Formik
        initialValues={initialValues || defaultValues}
        validationSchema={getValidationSchema()}
        onSubmit={isCreation ? handleCreate : handleUpdate}
      >
        {({
          errors,
          setErrors,
          touched,
          isSubmitting,
          setSubmitting,
          values,
          setFieldValue,
        }) => (
          <Form>
            <Box sx={{ marginBottom: "30px" }}>
              <Heading variant={TextVariant.H4}>
                {heading ? heading : t("newArticle.newArticleHeader.label")}
              </Heading>
            </Box>
            <Box sx={{ marginBottom: "20px" }}>
              <Field
                as={TextField}
                id="title"
                name="title"
                label={t("newArticle.title.label")}
                variant="standard"
                fullWidth
                error={touched.title && !!errors.title}
                helperText={touched.title && errors.title}
                InputProps={{
                  style: { fontSize: "18px" },
                }}
                InputLabelProps={{
                  style: { fontSize: "18px" },
                }}
              />
            </Box>
            <Box sx={{ marginBottom: "20px", display: "flex" }}>
              <Field
                as={TextField}
                id="content"
                name="content"
                label={t("newArticle.content.label")}
                variant="standard"
                fullWidth
                multiline
                error={touched.content && !!errors.content}
                helperText={touched.content && errors.content}
                InputProps={{
                  style: { fontSize: "18px" },
                }}
                InputLabelProps={{
                  style: { fontSize: "18px" },
                }}
              />
              <Button
                variant="contained"
                sx={{ marginLeft: "20px", marginY: "auto" }}
                disabled={isSubmitting || values.content.trim().length === 0}
                onClick={() => {
                  setSubmitting(true);

                  const currentContent: ContentGenerationFormValues = {
                    input: values.content,
                  };

                  generateContent(currentContent)
                    .then((content) => setFieldValue("content", content.data))
                    .finally(() => {
                      setSubmitting(false);
                    })
                    .catch(() =>
                      setErrors({
                        content: t("newArticle.aiError.label"),
                      }),
                    );
                }}
              >
                {t("newArticle.ai.label")}
              </Button>
            </Box>
            <Box sx={{ marginBottom: "20px" }}>
              <Autocomplete
                multiple
                options={tags}
                getOptionLabel={(option) =>
                  i18n.language === Language.EN ? option.engName : option.plName
                }
                renderOption={(props, option, { selected }) => (
                  <li {...props}>
                    <Checkbox checked={selected} style={{ marginRight: 8 }} />
                    {i18n.language === Language.EN
                      ? option.engName
                      : option.plName}
                  </li>
                )}
                value={values.tags}
                onChange={(event, value) => {
                  setFieldValue("tags", value);
                }}
                renderInput={(params) => (
                  <Field
                    as={TextField}
                    {...params}
                    variant="standard"
                    label={t("newArticle.tags.label")}
                    placeholder={t("newArticle.selectTags.label")}
                    error={touched.tags && !!errors.tags}
                    helperText={touched.tags && errors.tags}
                    InputProps={{
                      ...params.InputProps,
                      style: { fontSize: "18px" },
                    }}
                    InputLabelProps={{
                      style: { fontSize: "18px" },
                    }}
                  />
                )}
              />
            </Box>
            <Box sx={{ marginBottom: "20px" }}>
              <Button
                variant="contained"
                component="label"
                fullWidth
                startIcon={<UploadFileIcon />}
                onMouseOver={() => setShowImage(true)}
                onMouseOut={() => setShowImage(false)}
              >
                {imageAdded || initialValues?.image
                  ? t("newArticle.uploadedImage.label")
                  : t("newArticle.uploadImage.label")}
                <input
                  id="image"
                  name="image"
                  type="file"
                  accept="image/*"
                  hidden
                  onChange={(event) => {
                    const file = event.target.files?.[0];
                    if (file) {
                      file.arrayBuffer().then((buffer) => {
                        const image = new Uint8Array(buffer);
                        setFieldValue("image", Array.from(image));
                        setImageAdded(true);
                        setImagePreviewUrl(URL.createObjectURL(file));
                      });
                    }
                  }}
                />
              </Button>
              {touched.image && errors.image && (
                <Typography color="error" sx={{ marginTop: "10px" }}>
                  {errors.image}
                </Typography>
              )}
              {showImage && imagePreviewUrl && (
                <Box sx={{ marginTop: "10px", textAlign: "center" }}>
                  <img
                    src={imagePreviewUrl}
                    alt="Preview"
                    style={{
                      maxHeight: "50px",
                      maxWidth: "100%",
                    }}
                  />
                </Box>
              )}
            </Box>
            <Box>
              <Button
                color="primary"
                variant="contained"
                fullWidth
                type="submit"
                disabled={isSubmitting}
                startIcon={<PublishIcon />}
              >
                {t("newArticle.publish.label")}
              </Button>
            </Box>
          </Form>
        )}
      </Formik>
    </Box>
  );
};
