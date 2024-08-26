import { UserRoles } from "../../../constants/Const";
import { Field, Form, Formik, FormikHelpers } from "formik";
import { getValidationSchema, initialCommentValues } from "./validationSchema";
import { Card, CardContent, IconButton, TextField } from "@mui/material";
import DoneIcon from "@mui/icons-material/Done";
import React, { useContext, useState } from "react";
import {
  Comment,
  createComment,
  getArticleComments,
} from "../../../requests/articleList";
import { AuthContext } from "../../../providers/AuthProvider";
import { useTranslation } from "react-i18next";

type CommentsFormProps = {
  articleId: string;
  onCommentSubmitted: () => void;
};

type FormValues = {
  addComment: string;
};

export const CommentForm = ({
  articleId,
  onCommentSubmitted,
}: CommentsFormProps) => {
  // eslint-disable-next-line no-unused-vars
  const [comments, setComments] = useState<Comment[]>([]);
  const { loggedUser } = useContext(AuthContext);
  const { t } = useTranslation();

  const fetchComments = async () => {
    const comments = await getArticleComments(articleId);
    const sortedComments = comments.sort((a, b) => {
      if (a.userAccountUsername === loggedUser?.username) return -1;
      if (b.userAccountUsername === loggedUser?.username) return 1;
      return 0;
    });
    setComments(sortedComments);
  };

  const handleAddComment = async (
    values: FormValues,
    { setSubmitting, resetForm }: FormikHelpers<FormValues>,
  ) => {
    await createComment(articleId, values.addComment);
    setSubmitting(false);
    resetForm();
    onCommentSubmitted();
    fetchComments();
  };
  return (
    <>
      {(loggedUser?.role === UserRoles.ADMIN ||
        loggedUser?.role === UserRoles.USER) && (
        <Formik
          initialValues={initialCommentValues}
          validationSchema={getValidationSchema()}
          onSubmit={handleAddComment}
        >
          {({ errors, touched, isSubmitting }) => (
            <Form>
              <Card sx={{ marginBottom: "20px" }}>
                <CardContent sx={{ display: "flex", alignItems: "center" }}>
                  <Field
                    as={TextField}
                    name="addComment"
                    type="text"
                    label={t("articleContent.addComment.label")}
                    variant="standard"
                    fullWidth
                    multiline
                    error={touched.addComment && !!errors.addComment}
                    helperText={touched.addComment && errors.addComment}
                    InputProps={{
                      style: { fontSize: "18px", fontFamily: "Inter" },
                    }}
                    InputLabelProps={{
                      style: { fontSize: "18px", fontFamily: "Inter" },
                    }}
                    fontFamily="Inter"
                  />
                  <IconButton
                    type="submit"
                    disabled={isSubmitting}
                    sx={{ marginLeft: "20px" }}
                  >
                    <DoneIcon />
                  </IconButton>
                </CardContent>
              </Card>
            </Form>
          )}
        </Formik>
      )}
    </>
  );
};
