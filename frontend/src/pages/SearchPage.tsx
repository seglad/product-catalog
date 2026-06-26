import { useEffect, useState } from 'react';
import { listProducts, searchProducts } from '../api/client';
import { ProductCard } from '../components/ProductCard';
import { SearchBar } from '../components/SearchBar';
import type { Product } from '../types/product';

const DEBOUNCE_MS = 300;

export function SearchPage() {
  const [query, setQuery] = useState('');
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [hasSearched, setHasSearched] = useState(false);

  useEffect(() => {
    let cancelled = false;
    const trimmed = query.trim();

    setLoading(true);
    setError(null);

    if (!trimmed) {
      setHasSearched(false);

      listProducts()
        .then((results) => {
          if (!cancelled) {
            setProducts(results);
          }
        })
        .catch((err: unknown) => {
          if (!cancelled) {
            setProducts([]);
            setError(err instanceof Error ? err.message : 'Failed to load products');
          }
        })
        .finally(() => {
          if (!cancelled) {
            setLoading(false);
          }
        });

      return () => {
        cancelled = true;
      };
    }

    // Debounce search: wait 300ms after the user stops typing before calling the API.
    const timeoutId = window.setTimeout(() => {
      searchProducts(trimmed)
        .then((results) => {
          if (!cancelled) {
            setProducts(results);
            setHasSearched(true);
          }
        })
        .catch((err: unknown) => {
          if (!cancelled) {
            setProducts([]);
            setHasSearched(true);
            setError(err instanceof Error ? err.message : 'Search failed');
          }
        })
        .finally(() => {
          if (!cancelled) {
            setLoading(false);
          }
        });
    }, DEBOUNCE_MS);

    return () => {
      cancelled = true;
      window.clearTimeout(timeoutId);
    };
  }, [query]);

  return (
    <div className="search-page">
      <h1 className="page-title">Find furniture</h1>
      <SearchBar value={query} onChange={setQuery} />

      {loading && (
        <p className="status-message">
          {query.trim() ? 'Searching…' : 'Loading products…'}
        </p>
      )}

      {error && <p className="status-message status-message--error">{error}</p>}

      {!loading && !error && hasSearched && products.length === 0 && (
        <p className="status-message">No products matched your search.</p>
      )}

      {!loading && products.length > 0 && (
        <div className="product-grid">
          {products.map((product) => (
            <ProductCard key={product.id} product={product} />
          ))}
        </div>
      )}
    </div>
  );
}
