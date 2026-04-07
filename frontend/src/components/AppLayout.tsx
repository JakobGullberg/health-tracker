import { useState } from "react";
import Sidebar from "./Sidebar";
import "./AppLayout.css";

export default function AppLayout({ children }: { children: React.ReactNode }) {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <div className="app-layout">
      <Sidebar isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />

      <div className="app-main">
        <header className="app-topbar">
          <button
            className="topbar-toggle"
            onClick={() => setSidebarOpen(true)}
            aria-label="Open menu"
          >
            ☰
          </button>
        </header>

        <main className="app-content">{children}</main>
      </div>
    </div>
  );
}
