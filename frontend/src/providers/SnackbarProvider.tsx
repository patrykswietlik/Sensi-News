import { createContext, ReactNode, useState } from "react";
import Snackbar from "@mui/material/Snackbar";
import Alert from "@mui/material/Alert";

export interface SnackbarContextType {
  showSnackbar: (message: string) => void;
}

export const SnackbarContext = createContext<SnackbarContextType>({
  showSnackbar: () => {},
});

type SnackbarProviderProps = {
  children: ReactNode;
};

export const SnackbarProvider = ({ children }: SnackbarProviderProps) => {
  const [open, setOpen] = useState(false);
  const [message, setMessage] = useState<string>("");

  const showSnackbar = (message: string) => {
    setMessage(message);
    setOpen(true);
  };

  const hideSnackbar = () => {
    setOpen(false);
  };

  return (
    <SnackbarContext.Provider value={{ showSnackbar }}>
      {children}
      <Snackbar
        open={open}
        autoHideDuration={6000}
        onClose={hideSnackbar}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
      >
        <Alert onClose={hideSnackbar} severity="error" sx={{ width: "100%" }}>
          {message}
        </Alert>
      </Snackbar>
    </SnackbarContext.Provider>
  );
};
