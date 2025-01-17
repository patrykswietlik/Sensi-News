import { Box } from "@mui/material";
import { ScaffoldTemplate } from "../../components/templates/ScaffoldTemplate";
import { color } from "../../styles/colors";
import { TableForm } from "./Table";

export const EditTags = () => {
  return (
    <ScaffoldTemplate>
      <Box
        sx={{
          display: "flex",
          backgroundColor: color.gray25,
          minHeight: "80vh",
          height: "auto",
          paddingTop: "20px",
          paddingBottom: "20px",
        }}
      >
        <Box sx={{ width: { xs: "0%", md: "5%", lg: "10%", xl: "15%" } }} />
        <Box sx={{ height: "auto", width: "100%" }}>
          <TableForm />
        </Box>
        <Box sx={{ width: { xs: "0%", md: "5%", lg: "10%", xl: "15%" } }} />
      </Box>
    </ScaffoldTemplate>
  );
};
