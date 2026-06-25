interface SearchBarProps {
  value: string;
  onChange: (value: string) => void;
}

export function SearchBar({ value, onChange }: SearchBarProps) {
  return (
    <div className="search-bar">
      <input
        type="search"
        className="search-input"
        placeholder="Search for furniture by name…"
        value={value}
        onChange={(event) => onChange(event.target.value)}
        aria-label="Search products"
      />
    </div>
  );
}
