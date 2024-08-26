import { Alert, Snackbar } from "@mui/material";
import { useState } from "react";

interface SnackbarType {
  message: string;
}

const [openSnackbar, setOpenSnackbar] = useState(false);

const handleCloseSnackbar = () => {
  setOpenSnackbar(false);
};

export const ShowSnackbar = ({ message }: SnackbarType) => {
  return (
    <Snackbar
      open={openSnackbar}
      autoHideDuration={6000}
      onClose={handleCloseSnackbar}
    >
      <Alert
        onClose={handleCloseSnackbar}
        severity="error"
        sx={{ width: "100%" }}
      >
        {message}
      </Alert>
    </Snackbar>
  );
};
