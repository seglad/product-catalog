import type { ReactNode } from 'react';
import { Link } from 'react-router-dom';

interface LayoutProps {
  children: ReactNode;
}

export function Layout({ children }: LayoutProps) {
  return (
    <div className="layout">
      <header className="header">
        <Link to="/" className="logo">
          Product Catalog
        </Link>
      </header>
      <main className="main">{children}</main>
    </div>
  );
}
