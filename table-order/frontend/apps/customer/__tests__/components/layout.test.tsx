import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import { CustomerHeader } from '../../components/layout/CustomerHeader';
import { BottomNav } from '../../components/layout/BottomNav';
import { CustomerLayout } from '../../components/layout/CustomerLayout';

describe('CustomerHeader', () => {
  it('renders store name and table number', () => {
    render(<CustomerHeader storeName="테스트매장" tableNo={5} onSettingsClick={() => {}} />);
    expect(screen.getByText('테스트매장')).toBeInTheDocument();
    expect(screen.getByText('5')).toBeInTheDocument();
  });

  it('calls onSettingsClick when settings icon clicked', () => {
    const onSettingsClick = vi.fn();
    render(<CustomerHeader storeName="S" tableNo={1} onSettingsClick={onSettingsClick} />);
    fireEvent.click(screen.getByTestId('settings-btn'));
    expect(onSettingsClick).toHaveBeenCalled();
  });
});

describe('BottomNav', () => {
  it('renders 3 tabs', () => {
    render(<BottomNav currentPath="/menu" />);
    expect(screen.getByText('메뉴')).toBeInTheDocument();
    expect(screen.getByText('장바구니')).toBeInTheDocument();
    expect(screen.getByText('주문내역')).toBeInTheDocument();
  });

  it('highlights current path', () => {
    render(<BottomNav currentPath="/menu" />);
    expect(screen.getByTestId('nav-menu').className).toContain('active');
  });

  it('shows cart badge when cartItemCount > 0', () => {
    render(<BottomNav currentPath="/" cartItemCount={3} />);
    expect(screen.getByTestId('cart-badge')).toHaveTextContent('3');
  });

  it('hides cart badge when cartItemCount is 0', () => {
    render(<BottomNav currentPath="/" cartItemCount={0} />);
    expect(screen.queryByTestId('cart-badge')).not.toBeInTheDocument();
  });
});

describe('CustomerLayout', () => {
  it('renders children within layout', () => {
    render(<CustomerLayout><p>Content</p></CustomerLayout>);
    expect(screen.getByTestId('customer-layout')).toBeInTheDocument();
    expect(screen.getByText('Content')).toBeInTheDocument();
  });
});
