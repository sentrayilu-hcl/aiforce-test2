export const formFixture = {
  valid: {
    email: 'alex.chen@example.test',
    fullName: 'Alex Chen',
    phone: '555-010-4821',
    notes: 'Please deliver between 9 AM and 5 PM.'
  },
  invalid: {
    email: '',
    fullName: '',
    phone: 'invalid-phone',
    notes: ''
  },
  boundary: {
    email: 'a@b.co',
    fullName: 'A',
    phone: '555-010-0000',
    notes: 'x'.repeat(250)
  }
};
