import React from "react";
import { Footer } from "../../components/organisms/Footer";
import { Navbar } from "../../components/organisms/Navbar";

type TemplateProps = {
  children: React.ReactNode;
};

export const ScaffoldTemplate = ({ children }: TemplateProps) => {
  return (
    <>
      <Navbar />
      {children}
      <Footer />
    </>
  );
};
