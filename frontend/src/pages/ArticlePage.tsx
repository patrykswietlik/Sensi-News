import { Box } from "@mui/material";
import { ArticleContent } from "../components/organisms/ArticleContent";
import { ScaffoldTemplate } from "../components/templates/ScaffoldTemplate";
import { color } from "../styles/colors";

export const ArticlePage = () => {
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
        <Box sx={{ width: { xs: "0%", md: "5%", lg: "10%", xl: "20%" } }} />
        <ArticleContent />
        <Box sx={{ width: { xs: "0%", md: "5%", lg: "10%", xl: "20%" } }} />
      </Box>
    </ScaffoldTemplate>
  );
};
