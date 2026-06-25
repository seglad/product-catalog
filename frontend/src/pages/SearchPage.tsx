import { useEffect, useState } from 'react';
import { searchProducts } from '../api/client';
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

  // Debounce search: wait 300ms after the user stops typing before calling the API.
  // Each keystroke clears the previous timer (cleanup) and starts a new one.
  useEffect(() => {
    const trimmed = query.trim();

    if (!trimmed) {
      setProducts([]);
      setLoading(false);
      setError(null);
      setHasSearched(false);
      return;
    }

    setLoading(true);
    setError(null);

    const timeoutId = window.setTimeout(() => {
      searchProducts(trimmed)
        .then((results) => {
          setProducts(results);
          setHasSearched(true);
        })
        .catch((err: unknown) => {
          setProducts([]);
          setHasSearched(true);
          setError(err instanceof Error ? err.message : 'Search failed');
        })
        .finally(() => {
          setLoading(false);
        });
    }, DEBOUNCE_MS);

    return () => {
      window.clearTimeout(timeoutId);
    };
  }, [query]);

  return (
    <div className="search-page">
      <h1 className="page-title">Find furniture</h1>
      <SearchBar value={query} onChange={setQuery} />

      {!query.trim() && (
        <p className="status-message">Search for furniture by name to see results.</p>
      )}

      {loading && <p className="status-message">Searching…</p>}

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
