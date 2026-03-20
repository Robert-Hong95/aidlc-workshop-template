import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MenuForm } from '../../components/features/menu/MenuForm';
import { MenuItemRow } from '../../components/features/menu/MenuItemRow';
import { CategoryManager } from '../../components/features/menu/CategoryManager';
import { ImageUpload } from '../../components/features/menu/ImageUpload';

describe('MenuForm', () => {
  const categories = [{ id: 1, storeId: 1, name: 'Drinks', sortOrder: 0 }];

  it('renders all fields', () => {
    render(<MenuForm categories={categories} onSubmit={() => {}} isLoading={false} />);
    expect(screen.getByTestId('input-menu-name')).toBeInTheDocument();
    expect(screen.getByTestId('input-menu-price')).toBeInTheDocument();
    expect(screen.getByTestId('select-category')).toBeInTheDocument();
  });

  it('validates price range 100~1000000', async () => {
    render(<MenuForm categories={categories} onSubmit={() => {}} isLoading={false} />);
    fireEvent.change(screen.getByTestId('input-menu-name'), { target: { value: 'Coffee' } });
    fireEvent.change(screen.getByTestId('input-menu-price'), { target: { value: '50' } });
    fireEvent.change(screen.getByTestId('select-category'), { target: { value: '1' } });
    fireEvent.click(screen.getByTestId('menu-submit-btn'));
    await waitFor(() => {
      expect(screen.getByText(/100원/)).toBeInTheDocument();
    });
  });

  it('prefills data when editing', () => {
    const menu = { id: 1, categoryId: 1, name: 'Latte', price: 5000, sortOrder: 0 };
    render(<MenuForm categories={categories} onSubmit={() => {}} isLoading={false} menu={menu} />);
    expect(screen.getByTestId('input-menu-name')).toHaveValue('Latte');
    expect(screen.getByTestId('input-menu-price')).toHaveValue(5000);
  });
});

describe('MenuItemRow', () => {
  const menu = { id: 1, categoryId: 1, name: 'Coffee', price: 4000, sortOrder: 0 };

  it('renders menu info', () => {
    render(<table><tbody><MenuItemRow menu={menu} onEdit={() => {}} onDelete={() => {}} /></tbody></table>);
    expect(screen.getByText('Coffee')).toBeInTheDocument();
    expect(screen.getByText(/4,000/)).toBeInTheDocument();
  });

  it('calls onDelete when delete clicked', () => {
    const onDelete = vi.fn();
    render(<table><tbody><MenuItemRow menu={menu} onEdit={() => {}} onDelete={onDelete} /></tbody></table>);
    fireEvent.click(screen.getByTestId('menu-delete-btn'));
    expect(onDelete).toHaveBeenCalledWith(1);
  });
});

describe('CategoryManager', () => {
  const categories = [{ id: 1, storeId: 1, name: 'Drinks', sortOrder: 0 }];

  it('renders categories', () => {
    render(<CategoryManager categories={categories} onAdd={() => {}} onEdit={() => {}} onDelete={() => {}} />);
    expect(screen.getByText('Drinks')).toBeInTheDocument();
  });

  it('calls onDelete', () => {
    const onDelete = vi.fn();
    render(<CategoryManager categories={categories} onAdd={() => {}} onEdit={() => {}} onDelete={onDelete} />);
    fireEvent.click(screen.getByTestId('cat-delete-1'));
    expect(onDelete).toHaveBeenCalledWith(1);
  });
});

describe('ImageUpload', () => {
  it('renders upload area', () => {
    render(<ImageUpload onChange={() => {}} />);
    expect(screen.getByTestId('image-upload')).toBeInTheDocument();
  });
});
