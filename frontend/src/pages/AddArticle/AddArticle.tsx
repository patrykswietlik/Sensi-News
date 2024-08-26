import { Box } from "@mui/material";
import { ScaffoldTemplate } from "../../components/templates/ScaffoldTemplate";
import { color } from "../../styles/colors";
import { ArticleForm } from "./ArticleForm";

export const AddArticle = () => {
  return (
    <ScaffoldTemplate>
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          backgroundColor: color.gray25,
          minHeight: "80vh",
          height: "auto",
        }}
      >
        <Box sx={{ width: { xs: "0%", md: "5%", lg: "10%", xl: "20%" } }} />
        <Box sx={{ width: "100%" }}>
          <ArticleForm isCreation={true} />
        </Box>
        <Box sx={{ width: { xs: "0%", md: "5%", lg: "10%", xl: "20%" } }} />
      </Box>
    </ScaffoldTemplate>
  );
};
