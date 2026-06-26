import { describe, expect, it } from 'vitest';
import { formatPrice } from './client';

describe('formatPrice', () => {
  it('formats USD currency', () => {
    expect(formatPrice(899)).toBe('$899.00');
    expect(formatPrice(249.99)).toBe('$249.99');
  });
});
