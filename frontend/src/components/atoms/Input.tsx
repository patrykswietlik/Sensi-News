import * as React from "react";
import TextField from "@mui/material/TextField";

export type InputProps = {
  id: string;
  label: string;
  type: "password" | "number" | "text";
  handleChange?: (event: React.ChangeEvent<HTMLInputElement>) => void;
};

export const BasicInput = ({ id, label, type, handleChange }: InputProps) => {
  return (
    <div>
      <TextField
        required
        id={id}
        label={label}
        type={type}
        onChange={handleChange}
        sx={{ marginTop: "10px" }}
      />
    </div>
  );
};
