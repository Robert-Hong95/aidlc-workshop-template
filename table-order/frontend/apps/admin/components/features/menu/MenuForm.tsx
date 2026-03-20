import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Button } from '@table-order/ui';
import type { Menu, Category } from '@table-order/api-client';

const schema = z.object({
  name: z.string().min(1, '메뉴명을 입력해주세요').max(100),
  price: z.coerce.number().min(100, '100원 이상 입력해주세요').max(1000000, '1,000,000원 이하로 입력해주세요'),
  categoryId: z.coerce.number().min(1, '카테고리를 선택해주세요'),
  description: z.string().max(500).optional(),
  imageUrl: z.string().optional(),
});

type FormData = z.infer<typeof schema>;

export function MenuForm({ categories, onSubmit, isLoading, menu, onCancel }: {
  categories: Category[];
  onSubmit: (data: FormData) => void;
  isLoading: boolean;
  menu?: Partial<Menu>;
  onCancel?: () => void;
}) {
  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(schema),
    defaultValues: { name: menu?.name ?? '', price: menu?.price ?? undefined, categoryId: menu?.categoryId ?? undefined, description: menu?.description ?? '' },
  });

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-4">
      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium">메뉴명 *</label>
        <input {...register('name')} data-testid="input-menu-name" className="px-3 py-2 border rounded-md text-sm border-gray-300 focus:border-[#FF9900] outline-none" />
        {errors.name && <p className="text-xs text-red-500">{errors.name.message}</p>}
      </div>
      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium">가격 *</label>
        <input type="number" {...register('price')} data-testid="input-menu-price" className="px-3 py-2 border rounded-md text-sm border-gray-300 focus:border-[#FF9900] outline-none" />
        {errors.price && <p className="text-xs text-red-500">{errors.price.message}</p>}
      </div>
      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium">카테고리 *</label>
        <select {...register('categoryId')} data-testid="select-category" className="px-3 py-2 border rounded-md text-sm border-gray-300 focus:border-[#FF9900] outline-none">
          <option value="">선택</option>
          {categories.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}
        </select>
        {errors.categoryId && <p className="text-xs text-red-500">{errors.categoryId.message}</p>}
      </div>
      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium">설명</label>
        <textarea {...register('description')} data-testid="input-menu-desc" className="px-3 py-2 border rounded-md text-sm border-gray-300 focus:border-[#FF9900] outline-none" rows={3} />
      </div>
      <div className="flex gap-3 justify-end">
        {onCancel && <Button variant="secondary" onClick={onCancel}>취소</Button>}
        <Button type="submit" loading={isLoading} data-testid="menu-submit-btn">{menu ? '수정' : '등록'}</Button>
      </div>
    </form>
  );
}
