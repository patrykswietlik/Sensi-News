import CircularProgress from "@mui/material/CircularProgress";
import { Box } from "@mui/material";

export const LoadingSpinner = () => {
  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "70vh",
        width: "100%",
      }}
    >
      <CircularProgress />
    </Box>
  );
};
