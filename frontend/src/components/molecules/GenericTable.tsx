import React from "react";
import {
  IconButton,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import { useTranslation } from "react-i18next";
import { color } from "../../styles/colors";

export type DataObject = {
  [key: string]: string | React.ReactNode;
};

type ActionObject = {
  icon: React.ReactNode;
  // eslint-disable-next-line no-unused-vars
  onClick: (arg: DataObject) => void;
};

type LabelObject = {
  label: string;
  show: boolean;
};

interface GenericTableProps<T> {
  labels: LabelObject[];
  data: T[];
  actions?: ActionObject[];
  children?: React.ReactNode;
}

export const GenericTable = <T extends DataObject>({
  labels,
  data,
  actions,
  children,
}: GenericTableProps<T>) => {
  const { t } = useTranslation();

  return (
    <TableContainer sx={{ backgroundColor: color.white }}>
      <Table>
        <TableHead>
          <TableRow>
            {labels.map(
              (label, index) =>
                label.show && (
                  <TableCell
                    key={index}
                    sx={{ fontWeight: "bold", fontFamily: "Inter" }}
                  >
                    {label.label}
                  </TableCell>
                ),
            )}
            {actions && (
              <TableCell sx={{ fontFamily: "Inter", wordBreak: "break-word" }}>
                {t("editArticlesStatus.actions.label")}
              </TableCell>
            )}
          </TableRow>
        </TableHead>
        <TableBody>
          {data.map((row, rowIndex) => (
            <TableRow key={rowIndex}>
              {Object.entries(row).map(([key, value], cellIndex) => {
                if (labels[cellIndex].show) {
                  return (
                    <TableCell
                      key={key}
                      sx={{ fontFamily: "Inter", wordBreak: "break-word" }}
                    >
                      {value}
                    </TableCell>
                  );
                }
              })}
              {actions &&
                actions.length > 0 &&
                actions.map((action, index) => (
                  <IconButton
                    key={index}
                    onClick={() => {
                      action.onClick(row);
                    }}
                  >
                    {action.icon}
                  </IconButton>
                ))}
            </TableRow>
          ))}
        </TableBody>
      </Table>
      {children}
    </TableContainer>
  );
};
