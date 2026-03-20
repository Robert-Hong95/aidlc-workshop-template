import { useState, useRef } from 'react';

export function ImageUpload({ value, onChange }: {
  value?: string;
  onChange: (file: File | null, preview: string | null) => void;
}) {
  const resolveUrl = (url?: string) => {
    if (!url) return null;
    if (url.startsWith('data:') || url.startsWith('http')) return url;
    return `${process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080'}${url}`;
  };
  const [preview, setPreview] = useState<string | null>(resolveUrl(value));
  const inputRef = useRef<HTMLInputElement>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = () => {
      const result = reader.result as string;
      setPreview(result);
      onChange(file, result);
    };
    reader.readAsDataURL(file);
  };

  return (
    <div data-testid="image-upload" className="flex flex-col gap-2">
      <label className="text-sm font-medium">이미지</label>
      {preview && <img src={preview} alt="미리보기" className="w-32 h-32 object-cover rounded-md" data-testid="image-preview" />}
      <input ref={inputRef} type="file" accept=".jpg,.jpeg,.png,.webp" onChange={handleChange} className="text-sm" data-testid="image-input" />
    </div>
  );
}
