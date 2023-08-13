import axios from "axios";

export default async function handler(req, res) {
  if (req.method !== "DELETE") {
    res.status(405).end();
  }

  const { id } = req.query;
  const token = req.headers.authorization.split(" ")[1];

  const response = await await axios
    .delete(`${process.env.NEXT_PUBLIC_KOST_API_URL}/bundle/delete/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
    .then((response) => {
      if (response.status == 200) {
        toast.success(`Deleted Bundle with id ${id}`);
        res.status(200).json(response.data);
      }
    })
    .catch((error) => {
      res.status(400).json(error?.message);
    });
}
