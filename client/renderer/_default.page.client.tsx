import type { Root } from "react-dom/client";
import { createRoot, hydrateRoot } from "react-dom/client";
import { PageShell } from "./PageShell";
import type { PageContextClient } from "./types";

let root: Root;
export async function render(pageContext: PageContextClient) {
  const { Page, pageProps } = pageContext;
  if (!Page)
    throw new Error(
      "Client-side render() hook expects pageContext.Page to be defined"
    );
  const page = (
    <PageShell pageContext={pageContext}>
      <Page
        {...pageProps}
        searchParams={pageContext.urlParsed?.search ?? {}}
        params={pageContext.routeParams ?? {}}
      />
    </PageShell>
  );
  const container = document.getElementById("page-view")!;
  if (pageContext.isHydration) {
    root = hydrateRoot(container, page);
  } else {
    if (!root) {
      root = createRoot(container);
    }
    root.render(page);
  }
}

export const clientRouting = true;
export const hydrationCanBeAborted = true;
// !! WARNING !! Before doing so, read https://vite-plugin-ssr.com/clientRouting

export function onHydrationEnd() {
  console.log("Hydration finished; page is now interactive.");
}

export function onPageTransitionStart() {
  console.log("Page transition start");
  document.querySelector("body")!.classList.add("page-is-transitioning");
}

export function onPageTransitionEnd() {
  console.log("Page transition end");
  document.querySelector("body")!.classList.remove("page-is-transitioning");
}
