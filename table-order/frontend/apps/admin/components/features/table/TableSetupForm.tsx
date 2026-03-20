import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Button } from '@table-order/ui';

const schema = z.object({
  tableNo: z.coerce.number().min(1, '1 이상의 번호를 입력해주세요'),
  password: z.string().regex(/^\d{4}$/, '4자리 숫자를 입력해주세요'),
});

type FormData = z.infer<typeof schema>;

export function TableSetupForm({ onSubmit, isLoading }: {
  onSubmit: (data: FormData) => void;
  isLoading: boolean;
}) {
  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(schema),
  });

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-4" data-testid="table-setup-form">
      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-[#232F3E]">테이블번호</label>
        <input type="number" {...register('tableNo')} data-testid="input-table-no" className="px-3 py-2 border rounded-md text-sm border-gray-300 focus:border-[#FF9900] outline-none" />
        {errors.tableNo && <p className="text-xs text-red-500">{errors.tableNo.message}</p>}
      </div>
      <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-[#232F3E]">비밀번호 (4자리 PIN)</label>
        <input type="text" inputMode="numeric" maxLength={4} {...register('password')} data-testid="input-table-pin" className="px-3 py-2 border rounded-md text-sm border-gray-300 focus:border-[#FF9900] outline-none" />
        {errors.password && <p className="text-xs text-red-500">{errors.password.message}</p>}
      </div>
      <Button type="submit" loading={isLoading} data-testid="table-submit-btn">테이블 추가</Button>
    </form>
  );
}
