import { ResetPasswordForm } from "@components";
import { useRouter } from "next/router";

const ResetPassword = () => {
  const router = useRouter();
  const { token } = router.query;

  return (
    <div className="w-fit border-2 mx-auto mt-16 border-gray-600 align-middle rounded-md px-12 py-6">
      <ResetPasswordForm token={token} />
    </div>
  );
};

export default ResetPassword;
