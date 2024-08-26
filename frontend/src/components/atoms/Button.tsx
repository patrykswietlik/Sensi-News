import Button from "@mui/material/Button";

export type ButtonProps = {
  children: string;
  onClick?: () => void;
};

export const BasicButton = ({ children, onClick }: ButtonProps) => {
  return (
    <Button sx={{ marginTop: "10px" }} variant="contained" onClick={onClick}>
      {children}
    </Button>
  );
};
