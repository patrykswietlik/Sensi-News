import { Box, Button, Modal } from "@mui/material";
import { useTranslation } from "react-i18next";
import { color } from "../../styles/colors";
import { TextVariant } from "../../constants/Const";
import { Title } from "../atoms/Title";
import { ArticleInfo } from "../atoms/ArticleInfo";

export interface ArticlePreviewProps {
  id?: string;
  author?: string;
  title?: string;
  content?: string;
  image?: string;
  date?: Date;
}

interface ArticlePreviewModalProps extends ArticlePreviewProps {
  open: boolean;
  onClose: () => void;
}

export const ArticlePreviewModal = ({
  open,
  onClose,
  author,
  title,
  content,
  image,
  date,
}: ArticlePreviewModalProps) => {
  const { t } = useTranslation();

  return (
    <Modal open={open} onClose={onClose}>
      <Box
        sx={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          backgroundColor: color.white,
          maxHeight: "80vh",
          overflowY: "auto",
          boxShadow: 24,
          padding: "30px",
        }}
      >
        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            width: "100%",
            backgroundPosition: "center",
          }}
        >
          <img
            src={image}
            alt={title}
            style={{
              width: "100%",
              maxHeight: "80vh",
              objectFit: "cover",
            }}
          />
        </Box>

        <Title variant={TextVariant.H4} fontWeight="bold">
          {title || ""}
        </Title>
        <ArticleInfo
          author={author || ""}
          date={date || new Date()}
          contentColor={color.textPrimary}
        />

        <Box sx={{ padding: "20px" }}>{content || ""}</Box>
        <Box
          sx={{
            display: "flex",
            justifyContent: "space-between",
            paddingTop: "20px",
          }}
        >
          <Button variant="outlined" onClick={onClose}>
            {t("editArticlesStatus.close.label")}
          </Button>
        </Box>
      </Box>
    </Modal>
  );
};
