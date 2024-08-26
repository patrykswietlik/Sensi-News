import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";
import DoneIcon from "@mui/icons-material/Done";
import {
  IconButton,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
} from "@mui/material";
import { format } from "date-fns";
import { Field, Form, Formik, FormikHelpers } from "formik";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { ConfirmModal } from "../../components/molecules/ConfirmModal";
import { deleteTag, getAllTags, saveTag, Tag } from "../../requests/tags";
import { color } from "../../styles/colors";
import { getValidationSchema, initialTagValues } from "./validationSchema";
import { DATE_HOUR_PATTERN } from "../../constants/Const";

export const TableForm = () => {
  const [tags, setTags] = useState<Tag[]>([]);
  const { t } = useTranslation();
  const [openModal, setOpenModal] = useState(false);
  const [selectedTagId, setSelectedTagId] = useState<string | null>(null);

  const fetchTags = async () => {
    const tags = await getAllTags();
    setTags(tags);
  };

  useEffect(() => {
    fetchTags();
  }, []);

  const handleDelete = async (tagId: string) => {
    await deleteTag(tagId);
    fetchTags();
  };

  const confirmDelete = (tagId: string) => {
    setSelectedTagId(tagId);
    setOpenModal(true);
  };

  const handleModalClose = () => {
    setOpenModal(false);
    setSelectedTagId(null);
  };

  const handleModalConfirm = () => {
    if (selectedTagId) {
      handleDelete(selectedTagId);
    }
    handleModalClose();
  };

  const handleSaveNewTag = async (
    values: Tag,
    { setSubmitting, resetForm }: FormikHelpers<Tag>,
  ) => {
    await saveTag(values);
    setSubmitting(false);
    resetForm();
    fetchTags();
  };

  return (
    <TableContainer sx={{ padding: "30px", backgroundColor: "white" }}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell sx={{ fontWeight: "bold", fontFamily: "Inter" }}>
              {t("editTags.engName.label")}
            </TableCell>
            <TableCell sx={{ fontWeight: "bold", fontFamily: "Inter" }}>
              {t("editTags.plName.label")}
            </TableCell>
            <TableCell sx={{ fontWeight: "bold", fontFamily: "Inter" }}>
              {t("editTags.deletedOn.label")}
            </TableCell>
            <TableCell sx={{ fontWeight: "bold", fontFamily: "Inter" }} />
          </TableRow>
        </TableHead>
        <TableBody>
          {tags.map((tag) => (
            <TableRow key={tag.id}>
              <TableCell sx={{ fontFamily: "Inter" }}>{tag.engName}</TableCell>
              <TableCell sx={{ fontFamily: "Inter" }}>{tag.plName}</TableCell>
              {tag.deletedOn ? (
                <TableCell sx={{ color: color.deleted, fontFamily: "Inter" }}>
                  {format(new Date(tag.deletedOn), DATE_HOUR_PATTERN)}
                </TableCell>
              ) : (
                <TableCell sx={{ fontFamily: "Inter" }}>
                  {t("editTags.notApplicable.label")}
                </TableCell>
              )}
              <TableCell>
                {!tag.deletedOn && tag.removable && (
                  <IconButton onClick={() => confirmDelete(tag.id)}>
                    <DeleteOutlineIcon />
                  </IconButton>
                )}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      <Formik
        initialValues={initialTagValues}
        validationSchema={getValidationSchema()}
        onSubmit={handleSaveNewTag}
      >
        {({ errors, touched, isSubmitting }) => (
          <Form>
            <Table>
              <TableBody>
                <TableRow>
                  <TableCell></TableCell>
                  <TableCell>
                    <Field
                      as={TextField}
                      name="engName"
                      type="text"
                      label={t("editTags.engName.label")}
                      variant="standard"
                      fullWidth
                      error={touched.engName && !!errors.engName}
                      helperText={touched.engName && errors.engName}
                      InputProps={{
                        style: { fontSize: "18px", fontFamily: "Inter" },
                      }}
                      InputLabelProps={{
                        style: { fontSize: "18px", fontFamily: "Inter" },
                      }}
                      fontFamily="Inter"
                    />
                  </TableCell>
                  <TableCell>
                    <Field
                      as={TextField}
                      name="plName"
                      type="text"
                      label={t("editTags.plName.label")}
                      variant="standard"
                      fullWidth
                      error={touched.plName && !!errors.plName}
                      helperText={touched.plName && errors.plName}
                      InputProps={{
                        style: { fontSize: "18px", fontFamily: "Inter" },
                      }}
                      InputLabelProps={{
                        style: { fontSize: "18px", fontFamily: "Inter" },
                      }}
                      fontFamily="Inter"
                    />
                  </TableCell>
                  <TableCell></TableCell>
                  <TableCell>
                    <IconButton type="submit" disabled={isSubmitting}>
                      <DoneIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              </TableBody>
            </Table>
          </Form>
        )}
      </Formik>
      <ConfirmModal
        text={t("editTags.deleteConfirmation.label")}
        open={openModal}
        onClose={handleModalClose}
        onConfirm={handleModalConfirm}
      />
    </TableContainer>
  );
};
