import CalendarMonthIcon from "@mui/icons-material/CalendarMonth";
import PersonIcon from "@mui/icons-material/Person";
import { Box, Typography } from "@mui/material";
import { color } from "../../styles/colors";
import { format } from "date-fns";
import { DATE_HOUR_PATTERN } from "../../constants/Const";

type InfoProps = {
  author: string;
  date: Date;
  contentColor?: string;
};

export const ArticleInfo = ({
  author,
  date,
  contentColor = color.white,
}: InfoProps) => {
  return (
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        color: contentColor,
        paddingBottom: "10px",
      }}
    >
      <PersonIcon sx={{ width: "20px", paddingRight: "5px" }} />

      <Typography display="block" variant="caption" color={contentColor}>
        {author}
      </Typography>

      <CalendarMonthIcon
        sx={{ width: "20px", paddingRight: "5px", paddingLeft: "20px" }}
      />

      <Typography display="block" variant="caption" color={contentColor}>
        {format(date, DATE_HOUR_PATTERN)}
      </Typography>
    </Box>
  );
};
