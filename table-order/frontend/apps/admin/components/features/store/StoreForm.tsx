import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Button } from '@table-order/ui';

const schema = z.object({
  storeCode: z.string().min(1, '매장 코드를 입력해주세요'),
  name: z.string().min(1, '매장명을 입력해주세요').max(50),
});

type FormData = z.infer<typeof schema>;

export function StoreForm({ onSubmit, isLoading, store, onCancel }: {
  onSubmit: (data: FormData) => void;
  isLoading: boolean;
  store?: { storeCode?: string; name?: string };
  onCancel?: () => void;
}) {
  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(schema),
    defaultValues: { storeCode: store?.storeCode ?? '', name: store?.name ?? '' },
  });

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-4" data-testid="store-form">
      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-[#232F3E]">매장 코드 *</label>
        <input {...register('storeCode')} data-testid="input-store-code" className="px-3 py-2 border rounded-md text-sm border-gray-300 focus:border-[#FF9900] outline-none" />
        {errors.storeCode && <p className="text-xs text-red-500">{errors.storeCode.message}</p>}
      </div>
      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-[#232F3E]">매장명 *</label>
        <input {...register('name')} data-testid="input-store-name" className="px-3 py-2 border rounded-md text-sm border-gray-300 focus:border-[#FF9900] outline-none" />
        {errors.name && <p className="text-xs text-red-500">{errors.name.message}</p>}
      </div>
      <div className="flex gap-3 justify-end">
        {onCancel && <Button variant="secondary" onClick={onCancel}>취소</Button>}
        <Button type="submit" loading={isLoading} data-testid="store-submit-btn">{store ? '수정' : '등록'}</Button>
      </div>
    </form>
  );
}
