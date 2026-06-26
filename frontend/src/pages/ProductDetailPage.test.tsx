import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { getProductById } from '../api/client';
import { mockProduct } from '../test/fixtures';
import { ProductDetailPage } from './ProductDetailPage';

vi.mock('../api/client', async () => {
  const actual = await vi.importActual<typeof import('../api/client')>('../api/client');
  return {
    ...actual,
    getProductById: vi.fn(),
  };
});

function renderDetailPage(productId: string) {
  return render(
    <MemoryRouter initialEntries={[`/products/${productId}`]}>
      <Routes>
        <Route path="/products/:id" element={<ProductDetailPage />} />
      </Routes>
    </MemoryRouter>,
  );
}

describe('ProductDetailPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(getProductById).mockResolvedValue(mockProduct);
  });

  it('renders all product fields', async () => {
    renderDetailPage('1');

    expect(screen.getByText('Loading product…')).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByRole('heading', { name: mockProduct.name })).toBeInTheDocument();
    });

    expect(screen.getByText(mockProduct.category)).toBeInTheDocument();
    expect(screen.getByText(mockProduct.description)).toBeInTheDocument();
    expect(screen.getByText('$899.00')).toBeInTheDocument();
    expect(screen.getByRole('link', { name: mockProduct.imageUrl })).toBeInTheDocument();
    expect(getProductById).toHaveBeenCalledWith(1);
  });

  it('shows error when product is not found', async () => {
    vi.mocked(getProductById).mockRejectedValue(new Error('Product not found'));

    renderDetailPage('99');

    await waitFor(() => {
      expect(screen.getByText('Product not found')).toBeInTheDocument();
    });
  });

  it('shows error for invalid product id', async () => {
    renderDetailPage('not-a-number');

    await waitFor(() => {
      expect(screen.getByText('Invalid product id')).toBeInTheDocument();
    });

    expect(getProductById).not.toHaveBeenCalled();
  });
});
