import Layout from "@/pages/layout";
import React from "react";
import type { PageContext } from "./types";
import { PageContextProvider } from "./usePageContext";

export function PageShell({
  children,
  pageContext,
}: {
  children: React.ReactNode;
  pageContext: PageContext;
}) {
  return (
    <PageContextProvider pageContext={pageContext}>
      <Layout pageContext={pageContext}>{children}</Layout>
    </PageContextProvider>
  );
}
