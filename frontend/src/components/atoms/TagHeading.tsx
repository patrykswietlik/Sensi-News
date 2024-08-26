import { Box } from "@mui/material";

type TagHeadingProps = {
  tagName: string;
  color?: string;
};

export const TagHeading = ({ tagName, color }: TagHeadingProps) => {
  return (
    <Box
      sx={{
        fontSize: "13px",
        color: color,
        fontWeight: "bold",
      }}
    >
      {tagName}
    </Box>
  );
};
