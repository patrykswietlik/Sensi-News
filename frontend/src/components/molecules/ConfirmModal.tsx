import { Box, Button, Modal, Typography } from "@mui/material";
import { useTranslation } from "react-i18next";
import { color } from "../../styles/colors";

interface ConfirmModalProps {
  text: string;
  open: boolean;
  onClose: () => void;
  onConfirm: () => void;
}

export const ConfirmModal = ({
  text,
  open,
  onClose,
  onConfirm,
}: ConfirmModalProps) => {
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
          boxShadow: 24,
          padding: "30px",
        }}
      >
        <Typography variant="h6" component="h2">
          {text}
        </Typography>
        <Box
          sx={{
            display: "flex",
            justifyContent: "space-between",
            paddingTop: "20px",
          }}
        >
          <Button variant="contained" onClick={onConfirm}>
            {t("editTags.confirm.label")}
          </Button>
          <Button variant="outlined" onClick={onClose}>
            {t("editTags.cancel.label")}
          </Button>
        </Box>
      </Box>
    </Modal>
  );
};
