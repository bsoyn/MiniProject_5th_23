export const purchaseBook = async (readerId, bookId) => {
  try {
    const response = await fetch('http://localhost:8088/purchasedBooks/purchasebook', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ readerId, bookId }),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    throw error;
  }
};
