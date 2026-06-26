import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { listProducts, searchProducts } from '../api/client';
import { mockProduct, mockProduct2 } from '../test/fixtures';
import { SearchPage } from './SearchPage';

vi.mock('../api/client', async () => {
  const actual = await vi.importActual<typeof import('../api/client')>('../api/client');
  return {
    ...actual,
    listProducts: vi.fn(),
    searchProducts: vi.fn(),
  };
});

describe('SearchPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(listProducts).mockResolvedValue([mockProduct, mockProduct2]);
    vi.mocked(searchProducts).mockResolvedValue([mockProduct]);
  });

  it('loads all products on mount', async () => {
    render(
      <MemoryRouter>
        <SearchPage />
      </MemoryRouter>,
    );

    expect(await screen.findByText('Modern Sectional Sofa')).toBeInTheDocument();
    expect(screen.getByText('Oak Coffee Table')).toBeInTheDocument();
    expect(listProducts).toHaveBeenCalledTimes(1);
    expect(searchProducts).not.toHaveBeenCalled();
  });

  it('searches after debounce when user types', async () => {
    render(
      <MemoryRouter>
        <SearchPage />
      </MemoryRouter>,
    );

    await screen.findByText('Modern Sectional Sofa');

    fireEvent.change(screen.getByRole('searchbox'), { target: { value: 'sofa' } });

    await waitFor(() => {
      expect(searchProducts).toHaveBeenCalledWith('sofa');
    });
  });

  it('shows message when search returns no results', async () => {
    vi.mocked(searchProducts).mockResolvedValue([]);

    render(
      <MemoryRouter>
        <SearchPage />
      </MemoryRouter>,
    );

    await screen.findByText('Modern Sectional Sofa');

    fireEvent.change(screen.getByRole('searchbox'), { target: { value: 'xyz' } });

    await waitFor(() => {
      expect(screen.getByText('No products matched your search.')).toBeInTheDocument();
    });
  });
});
