import { ApplicationInitStatus } from '../models/application-init-status.model';
import { UIApplicationStage } from '../models/ui-application-stage.model';

export function mapStatusToStage(status: ApplicationInitStatus): UIApplicationStage {
  switch (status) {
    case 'USER_CREATED':
      return 'CREATED';

    case 'APPLIED':
      return 'SUBMITTED';

    case 'DOCUMENT_VERIFICATION_PENDING':
    case 'DOCUMENT_RETURNED':
    case 'DOCUMENT_APPROVED':
    case 'DOCUMENT_REJECTED':
      return 'DOCUMENT_VERIFICATION';

    case 'LOAN_APPROVED':
    case 'SANCTION_ACCEPTED':
      return 'LOAN_APPROVAL';

    case 'LOAN_DISBURSED':
      return 'DISBURSED';

    case 'LOAN_CLOSED':
      return 'CLOSED';

    default:
      return 'CREATED';
  }
}
