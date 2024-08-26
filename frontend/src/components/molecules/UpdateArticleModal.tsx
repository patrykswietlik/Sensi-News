import { Box, Button, Modal } from "@mui/material";
import { useTranslation } from "react-i18next";
import { color } from "../../styles/colors";
import { ArticleForm } from "../../pages/AddArticle/ArticleForm";
import { ArticleCreateFormValues, deleteArticle } from "../../requests/article";
import ClearIcon from "@mui/icons-material/Clear";
import DeleteIcon from "@mui/icons-material/Delete";
import { ConfirmModal } from "./ConfirmModal";
import { useState } from "react";
import { RoutePath } from "../../router/RoutePath ";
import { useNavigate } from "react-router-dom";

interface UpdateArticleModalProps {
  open: boolean;
  onClose: () => void;
  initialValues?: ArticleCreateFormValues;
  fetchData?: () => void;
}

export const UpdateArticleModal = ({
  open,
  onClose,
  initialValues,
  fetchData,
}: UpdateArticleModalProps) => {
  const { t } = useTranslation();
  const [openModal, setOpenModal] = useState(false);
  const navigate = useNavigate();

  const articleUpdate = () => {
    onClose();
    if (fetchData) {
      fetchData();
    }
  };

  const handleDelete = async (id: string) => {
    await deleteArticle(id);
    navigate(RoutePath.START);
  };

  const confirmDelete = () => {
    setOpenModal(true);
  };

  const handleModalClose = () => {
    setOpenModal(false);
  };

  const handleModalConfirm = () => {
    if (initialValues?.articleId) {
      handleDelete(initialValues?.articleId);
    }
    handleModalClose();
  };

  return (
    <>
      <Modal open={open} onClose={onClose}>
        <Box
          sx={{
            position: "absolute",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            width: "80%",
            height: "80%",
            display: "flex",
            flexDirection: "column",
            backgroundColor: color.white,
            boxShadow: 24,
            overflowY: "auto",
          }}
        >
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              padding: "20px",
            }}
          >
            <Box>
              <Button
                color="primary"
                variant="contained"
                fullWidth
                type="submit"
                onClick={confirmDelete}
                startIcon={<DeleteIcon />}
              >
                {t("newArticle.deleteArticle.label")}
              </Button>
            </Box>
            <Box>
              <Button
                color="primary"
                variant="contained"
                fullWidth
                type="submit"
                onClick={onClose}
                startIcon={<ClearIcon />}
              >
                {t("editArticlesStatus.close.label")}
              </Button>
            </Box>
          </Box>
          <Box>
            <ArticleForm
              initialValues={initialValues}
              heading={t("newArticle.editArticleHeader.label")}
              isCreation={false}
              onArticelUpdate={articleUpdate}
            />
          </Box>
        </Box>
      </Modal>
      <ConfirmModal
        text={t("newArticle.deleteArticleConfirmation.label")}
        open={openModal}
        onClose={handleModalClose}
        onConfirm={handleModalConfirm}
      />
    </>
  );
};
