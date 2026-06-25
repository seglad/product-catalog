import { Route, Routes } from 'react-router-dom';
import { Layout } from './components/Layout';
import { ProductDetailPage } from './pages/ProductDetailPage';
import { SearchPage } from './pages/SearchPage';

export function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<SearchPage />} />
        <Route path="/products/:id" element={<ProductDetailPage />} />
      </Routes>
    </Layout>
  );
}
