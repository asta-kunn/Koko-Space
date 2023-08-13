import axios from "axios";

export default async function handler(req, res) {
  if (req.method !== "PATCH") {
    res.status(405).end();
  }
  const body = req.body;
  const { id } = req.query;
  const token = req.headers.authorization.split(" ")[1];

  try {
    const response = await axios.patch(
      `${process.env.NEXT_PUBLIC_KOST_API_URL}/kost/update/${id}`,
      {
        ...body,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    res.status(200).json(response.data);
  } catch (e) {
    console.log(e);
    res.status(e.response.status).json(e.response.data);
  }
}
