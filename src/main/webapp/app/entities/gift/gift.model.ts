import { BaseEntity, User } from './../../shared';

export class Gift implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public fileContentType?: string,
        public file?: any,
        public description?: string,
        public price?: number,
        public user?: User,
    ) {
    }
}
