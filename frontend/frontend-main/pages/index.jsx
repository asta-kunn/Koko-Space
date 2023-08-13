import { useAuthContext } from "components/context/AuthContext";
export default function Home() {
  const { user } = useAuthContext();

  return (
    <div>
      <div className="text-5xl text-center py-10">Welcome to Koko Space</div>
    </div>
  );
}
