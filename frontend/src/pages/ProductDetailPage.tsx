import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { formatPrice, getProductById } from '../api/client';
import type { Product } from '../types/product';

export function ProductDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const productId = Number(id);

    if (!id || Number.isNaN(productId)) {
      setError('Invalid product id');
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);

    getProductById(productId)
      .then(setProduct)
      .catch((err: unknown) => {
        setProduct(null);
        setError(err instanceof Error ? err.message : 'Failed to load product');
      })
      .finally(() => {
        setLoading(false);
      });
  }, [id]);

  if (loading) {
    return <p className="status-message">Loading product…</p>;
  }

  if (error) {
    return (
      <div className="detail-page">
        <Link to="/" className="back-link">
          ← Back to search
        </Link>
        <p className="status-message status-message--error">{error}</p>
      </div>
    );
  }

  if (!product) {
    return null;
  }

  return (
    <div className="detail-page">
      <Link to="/" className="back-link">
        ← Back to search
      </Link>

      <article className="product-detail">
        {product.imageUrl && (
          <img
            src={product.imageUrl}
            alt={product.name}
            className="product-detail__image"
          />
        )}
        <div className="product-detail__content">
          <h1 className="product-detail__name">{product.name}</h1>
          <dl className="product-detail__fields">
            <div className="product-detail__field">
              <dt>Category</dt>
              <dd>{product.category}</dd>
            </div>
            <div className="product-detail__field">
              <dt>Price</dt>
              <dd>{formatPrice(product.price)}</dd>
            </div>
            <div className="product-detail__field">
              <dt>Description</dt>
              <dd>{product.description}</dd>
            </div>
            <div className="product-detail__field">
              <dt>Image URL</dt>
              <dd>
                <a href={product.imageUrl} target="_blank" rel="noreferrer">
                  {product.imageUrl}
                </a>
              </dd>
            </div>
          </dl>
        </div>
      </article>
    </div>
  );
}
