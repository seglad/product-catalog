import type { Product } from '../types/product';

const API_BASE = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

async function handleResponse<T>(response: Response): Promise<T> {
  if (response.ok) {
    return response.json() as Promise<T>;
  }

  if (response.status === 404) {
    throw new Error('Product not found');
  }

  throw new Error('Something went wrong. Please try again.');
}

interface ProductPage {
  content: Product[];
}

// List all products limited to 100 items, don't use pagination for now.
export async function listProducts(): Promise<Product[]> {
  const response = await fetch(`${API_BASE}/products?page=0&size=100&sort=name,asc`);
  const page = await handleResponse<ProductPage>(response);
  return page.content;
}

export async function searchProducts(query: string): Promise<Product[]> {
  const url = `${API_BASE}/search?q=${encodeURIComponent(query)}`;
  const response = await fetch(url);
  return handleResponse<Product[]>(response);
}

export async function getProductById(id: number): Promise<Product> {
  const response = await fetch(`${API_BASE}/products/${id}`);
  return handleResponse<Product>(response);
}

export function formatPrice(price: number): string {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(price);
}
