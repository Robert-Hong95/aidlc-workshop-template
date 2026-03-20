import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { AdminHeader } from '../../components/layout/AdminHeader';
import { Sidebar } from '../../components/layout/Sidebar';
import { AdminLayout } from '../../components/layout/AdminLayout';

describe('AdminHeader', () => {
  it('renders store name and buttons', () => {
    const onMenuToggle = vi.fn();
    const onLogout = vi.fn();
    render(<AdminHeader storeName="테스트매장" onMenuToggle={onMenuToggle} onLogout={onLogout} />);
    expect(screen.getByText('테스트매장')).toBeInTheDocument();
    expect(screen.getByTestId('admin-header')).toBeInTheDocument();
  });

  it('calls onLogout when logout button clicked', () => {
    const onLogout = vi.fn();
    render(<AdminHeader storeName="S" onMenuToggle={() => {}} onLogout={onLogout} />);
    fireEvent.click(screen.getByTestId('logout-btn'));
    expect(onLogout).toHaveBeenCalled();
  });

  it('calls onMenuToggle when hamburger clicked', () => {
    const onMenuToggle = vi.fn();
    render(<AdminHeader storeName="S" onMenuToggle={onMenuToggle} onLogout={() => {}} />);
    fireEvent.click(screen.getByTestId('menu-toggle'));
    expect(onMenuToggle).toHaveBeenCalled();
  });
});

describe('Sidebar', () => {
  it('renders nav items', () => {
    render(<Sidebar collapsed={false} onToggle={() => {}} currentPath="/dashboard" />);
    expect(screen.getByTestId('sidebar')).toBeInTheDocument();
    expect(screen.getByText('대시보드')).toBeInTheDocument();
  });

  it('highlights current path', () => {
    render(<Sidebar collapsed={false} onToggle={() => {}} currentPath="/dashboard" />);
    const dashboardLink = screen.getByTestId('nav-dashboard');
    expect(dashboardLink.className).toContain('active');
  });

  it('calls onToggle when toggle button clicked', () => {
    const onToggle = vi.fn();
    render(<Sidebar collapsed={false} onToggle={onToggle} currentPath="/" />);
    fireEvent.click(screen.getByTestId('sidebar-toggle'));
    expect(onToggle).toHaveBeenCalled();
  });
});

describe('AdminLayout', () => {
  it('renders children within layout', () => {
    render(<AdminLayout><p>Content</p></AdminLayout>);
    expect(screen.getByTestId('admin-layout')).toBeInTheDocument();
    expect(screen.getByText('Content')).toBeInTheDocument();
  });
});
