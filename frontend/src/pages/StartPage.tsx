import { Box } from "@mui/material";
import { StartArticles } from "../components/organisms/StartArticles";
import { ScaffoldTemplate } from "../components/templates/ScaffoldTemplate";
import { color } from "../styles/colors";

export const StartPage = () => {
  return (
    <ScaffoldTemplate>
      <Box
        sx={{
          display: "flex",
          backgroundColor: color.gray25,
          minHeight: "80vh",
          height: "auto",
        }}
      >
        <Box sx={{ width: { xs: "0%", md: "5%", lg: "10%", xl: "15%" } }} />
        <StartArticles />
        <Box sx={{ width: { xs: "0%", md: "5%", lg: "10%", xl: "15%" } }} />
      </Box>
    </ScaffoldTemplate>
  );
};
